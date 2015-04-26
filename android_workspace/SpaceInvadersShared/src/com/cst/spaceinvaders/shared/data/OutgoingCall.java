package com.cst.spaceinvaders.shared.data;

public final class OutgoingCall
{
    public String m_to;

    public OutgoingCall(String to)
    {
        m_to = to;
    }

    @Override
    public String toString()
    {
        return String.format("[%s]", m_to);
    }
}
