package com.societegenerale.banking;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Raveh on 03/10/2014.
 */
public class CustomByteBuffer
{
    private ByteArrayOutputStream _outBuffer;
    ByteArrayInputStream _inputBuffer;
    int _lastVarIntSize;

    CustomByteBuffer()
    {
        _outBuffer = new ByteArrayOutputStream();
        _inputBuffer = new ByteArrayInputStream(_outBuffer.toByteArray());
        _lastVarIntSize = 0;
    }

    private boolean IsLittleEndian()
    {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN))
            return false;
        else
            return true;
    }

    private void ReverseArray(byte[] array)
    {
        int stop = array.length / 2;
        for(int i = 0; i < stop; i++)
        {
            byte temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    public int ReadVarInt()
    {
        int shift = 0;
        int result = 0;
        int sizeBites = 32;
        int byteValue;
        int i = 0;

        _lastVarIntSize = 0;
        while (_inputBuffer.available() != 0)
        {
            byteValue = _inputBuffer.read();
            _lastVarIntSize++;
            int tmp = byteValue & 0x7f;
            result |= tmp << shift;
            if (shift > sizeBites)
            {
                throw new RuntimeException("Byte array is too large.");
            }
            if ((byteValue & 0x80) != 0x80)
            {
                return result;
            }
            shift += 7;
            i++;
        }
        throw new RuntimeException("Cannot decode varint from byte array.");
    }

    public int ReadInt()
    {
        byte[]  buf = new byte[4];
        _inputBuffer.read(buf, 0, 4);
        if (!IsLittleEndian())
            ReverseArray(buf);
        ByteBuffer wrapped = ByteBuffer.wrap(buf); // big-endian by default
        return wrapped.getInt();
    }

    public void WriteVarInt(int value)
    {
        byte[] bytes = new byte[5];
        int idx = 0;

        do
        {
            bytes[idx] = (byte)((value & 0x7f) | ((value > 0x7f) ? 0x80 : 0x00));
            value = value >> 7;
            idx++;
        } while (value > 0);
        _outBuffer.write(bytes, 0, idx);
        _inputBuffer = new ByteArrayInputStream(_outBuffer.toByteArray());
    }

    public void WriteInt(int value)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(4);
        dbuf.putInt(value);
        byte[] bytes = dbuf.array();
        _outBuffer.write(bytes, 0, bytes.length);
        _inputBuffer = new ByteArrayInputStream(_outBuffer.toByteArray());
    }

    public void Write(byte[] buf)
    {
        _outBuffer.write(buf, 0, buf.length);
        _inputBuffer = new ByteArrayInputStream(_outBuffer.toByteArray());
    }

    public void Write(byte[] buf, int offset, int length)
    {
        _outBuffer.write(buf, offset, length);
        _inputBuffer = new ByteArrayInputStream(_outBuffer.toByteArray());
    }

    public byte[] GetBuffer()
    {
        return _outBuffer.toByteArray();
    }

    public int GetLength()
    {
        return _outBuffer.size();
    }

    public void Mark() { _inputBuffer.mark(1024); }

    public void Reset() { _inputBuffer.reset(); }

    public int AvailableToRead() { return _inputBuffer.available(); }

    public int GetLastVarIntSize() { return _lastVarIntSize; }

    public void Skip(int byteCount)
    {
        byte[]  buf = _outBuffer.toByteArray();
        _outBuffer = new ByteArrayOutputStream();
        _outBuffer.write(buf, byteCount, buf.length - byteCount);
        _inputBuffer = new ByteArrayInputStream(_outBuffer.toByteArray());
    }
}
