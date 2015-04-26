package com.cst.spaceinvaders.shared.data;

public final class OutgoingSMS
{
    public String m_to;
    public String m_message;

    public OutgoingSMS()
    {
        this("undefined", "undefined");
    }

    public OutgoingSMS(String to, String message)
    {
        m_to = to;
        m_message = message;
    }

    @Override
    public String toString()
    {
        return "[" + m_to + "]: " + m_message;
    }
}
