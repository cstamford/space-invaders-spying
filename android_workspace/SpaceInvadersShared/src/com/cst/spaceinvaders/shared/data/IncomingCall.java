package com.cst.spaceinvaders.shared.data;

public final class IncomingCall
{
    public String m_from;

    public IncomingCall(String from)
    {
        m_from = from;
    }

    @Override
    public String toString()
    {
        return String.format("[%s]", m_from);
    }
}
