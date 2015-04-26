package com.cst.spaceinvaders.shared.data;

public final class IncomingSMS
{
    public String m_from;
    public String m_message;

    public IncomingSMS()
    {
        this("undefined", "undefined");
    }

    public IncomingSMS(String from, String message)
    {
        m_from = from;
        m_message = message;
    }

    @Override
    public String toString()
    {
        return "[" + m_from + "]: " + m_message;
    }
}
