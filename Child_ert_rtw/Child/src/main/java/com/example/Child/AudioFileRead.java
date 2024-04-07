package com.example.Child;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class AudioFileRead {

public static final int MAX_OLD_API_LEVEL = 20;
    private AssetFileDescriptor mAudioFileDescriptor;
    private Context mContext = null;

    MediaExtractor mMediaExtractor;
    MediaCodec mAudioDecoder;
    MediaFormat mMediaFormat;

    //For API <= MAX_OLD_API_LEVEL
    ByteBuffer[] codecInputBuffers;
    ByteBuffer[] codecOutputBuffers;

    // Audio file info
    String mMime = null; //audio file type
    long mSampleTime = 0l;
    int mSamplingFrequency = 0;
    long mDuration = 0l; //duration in microseconds

    int mApkVersion = 0;

    int mBlockOutputSize = 0;

    ArrayList<Short> byteArrayListBuffer = new ArrayList<>();

    //For buffering
    int mAudioSampleCounter = 0;

    private String mFileName;
    private int mFrameSize = 1024;
    private int mNumChannels = 1;

    private short[] mOutData;
    private int mOutDataIdx = 0;
    private short[] mDataBuffer;

    private short[] mDataRemainderBuffer;
    private int mRemainderSize=0;
    /**
     * Constructor - init
     **/
    AudioFileRead(Child mainActivity, String fileName, int frameSize) {
        mFileName = fileName;
        mFrameSize = frameSize;

        mContext = mainActivity;
        mApkVersion = android.os.Build.VERSION.SDK_INT;

        int fileId = mContext.getResources().getIdentifier(mFileName, "raw", mContext.getPackageName());
        mAudioFileDescriptor = mContext.getResources().openRawResourceFd(fileId);

        mMediaExtractor = new MediaExtractor();
        try {
            mMediaExtractor.setDataSource(mAudioFileDescriptor.getFileDescriptor(),
                    mAudioFileDescriptor.getStartOffset(), mAudioFileDescriptor.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSampleTime = mMediaExtractor.getSampleTime();
        mMediaFormat = mMediaExtractor.getTrackFormat(0);

        mMime = mMediaFormat.getString(MediaFormat.KEY_MIME);
        mNumChannels = mMediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        mSamplingFrequency = mMediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        mDuration = mMediaFormat.getLong((MediaFormat.KEY_DURATION));

        mBlockOutputSize = mFrameSize * mNumChannels;

        mOutData = new short[mBlockOutputSize];
        mDataRemainderBuffer = new short[mBlockOutputSize];
        //Create the audio decoder object
        try {
            mAudioDecoder = MediaCodec.createDecoderByType(mMime);
        } catch (IOException e) {
            e.printStackTrace();

        }
        //Configure the audio decoder object
        mAudioDecoder.configure(
                mMediaFormat,
                null,//surface, for video only
                null,//crypto, for decryption, null for non-secure codecs
                0);//CONFIGURE_FLAG_ENCODE, only for encoding

        mAudioDecoder.start();
        if(mApkVersion <= MAX_OLD_API_LEVEL) {
            codecInputBuffers = mAudioDecoder.getInputBuffers();
            codecOutputBuffers = mAudioDecoder.getOutputBuffers();
        }

        //Select a track, for reading samples from the media from this track
        //For audio, the track number is 0
        mMediaExtractor.selectTrack(0);

    }

    /**
     * Step 
     * @return short array of size [NxM] where N is the selected frame size, M is the number of channels
     **/
    public short[] AudioFileReadStep() {
        try {
            boolean sawInputEOS = false;
            boolean sawOutputEOS = false;

            while(mAudioSampleCounter + mRemainderSize < mBlockOutputSize) {
                //dequeueInputBuffer - Returns the index of an input buffer to be filled with valid data or -1 if no such buffer is currently available.
                for(int i = 0; i < mAudioSampleCounter; ++i) {
                    mDataRemainderBuffer[mRemainderSize++] = mDataBuffer[mOutDataIdx++];
                }
                mAudioSampleCounter = 0;
                int inputBufIndex = mAudioDecoder.dequeueInputBuffer(-1);
                if (inputBufIndex >= 0) {
                    ByteBuffer dstBuf = null;
                    if (mApkVersion <= MAX_OLD_API_LEVEL) { 
                        dstBuf = codecInputBuffers[inputBufIndex];
                    } else { 
                        dstBuf = mAudioDecoder.getInputBuffer(inputBufIndex);
                    }

                    //readSampleData - Retrieve the current encoded sample and store it in the byte buffer starting at the given offset.
                    int sampleSize = mMediaExtractor.readSampleData(dstBuf, 0);

                    long presentationTimeUs = 0;
                    if (sampleSize < 0) {
                        sawInputEOS = true;
                        sampleSize = 0;
                    } else {
                        sawInputEOS = false;
                        presentationTimeUs = mMediaExtractor.getSampleTime();
                    }

                    mAudioDecoder.queueInputBuffer(inputBufIndex,
                            0, //offset
                            sampleSize,
                            presentationTimeUs,
                            0);
                    if (!sawInputEOS) {
                        mMediaExtractor.advance();
                    } else {
                        mMediaExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
                        continue;
                    }
                    MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

                    // decode to PCM(raw) data
                    final int res = mAudioDecoder.dequeueOutputBuffer(info, 5000);
                    if (res >= 0) { //process the buffer
                        int outputBufIndex = res;

                        ByteBuffer buf = null;

                        if (mApkVersion <= MAX_OLD_API_LEVEL) { 
                            mDataBuffer = new short[info.size/2];
                            buf = codecOutputBuffers[outputBufIndex];
                        } else { 
                            buf = mAudioDecoder.getOutputBuffer(outputBufIndex);
                            mDataBuffer = new short[(buf.limit() - buf.position())/2];
                        }

                        buf.order(ByteOrder.nativeOrder()).asShortBuffer().get(mDataBuffer);
                        mOutDataIdx = 0;
                        mAudioSampleCounter += mDataBuffer.length;
                        mAudioDecoder.releaseOutputBuffer(outputBufIndex, false /* render */);

                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            sawOutputEOS = true;
                        }
                    } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        if (mApkVersion <= 20) {
                            codecOutputBuffers = mAudioDecoder.getOutputBuffers();
                        }
                    } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    }
                }
            }//end of while loop

            int frameIdx = 0;
            int remainderIdx = 0;
            if (mRemainderSize > 0) {
                for(; frameIdx < mRemainderSize/mNumChannels; ++frameIdx) {
                    for(int j = 0; j < mNumChannels; ++j) {
                        mOutData[j*mFrameSize + frameIdx] = mDataRemainderBuffer[remainderIdx++];
                    }
                }
                mRemainderSize = 0;
            }
            int counter = 0;
            for(; frameIdx < mFrameSize; ++frameIdx) {
                for(int j = 0; j < mNumChannels; ++j) {
                    mOutData[j*mFrameSize + frameIdx] = mDataBuffer[mOutDataIdx++];
                    counter++;
                }
            }
           mAudioSampleCounter -= counter;
        } catch (IllegalStateException ise) {
            if(mAudioDecoder != null) {
                mAudioDecoder.release();
                mAudioDecoder = null;
            }
            if(mMediaExtractor != null) {
            mMediaExtractor.release();
                mMediaExtractor = null;
            }
        }

        return mOutData;

    }

    /**
     * Terminate 
     **/
    public void AudioFileReadTerminate() {
        if(mAudioDecoder != null) {
            mAudioDecoder.release();
            mAudioDecoder = null;
        }
        if(mMediaExtractor != null) {
            mMediaExtractor.release();
            mMediaExtractor = null;
        }
    }

}
