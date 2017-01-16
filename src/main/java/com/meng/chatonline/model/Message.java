package com.meng.chatonline.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author xindemeng
 */
@Entity
@Table(name="messages")
public class Message implements Serializable
{
    private static final long serialVersionUID = -4300605539487532221L;

    private Integer id;
    private User toUser;
    private User fromUser;
    private String text;
    //发送时间
    private Date date;

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @JoinColumn(name="toUser_id", nullable = false)
    @ManyToOne
    public User getToUser()
    {
        return toUser;
    }

    public void setToUser(User toUser)
    {
        this.toUser = toUser;
    }

    @JoinColumn(name="fromUser_id", nullable = false)
    @ManyToOne
    public User getFromUser()
    {
        return fromUser;
    }

    public void setFromUser(User fromUser)
    {
        this.fromUser = fromUser;
    }

    //设置为 text 类型
    @Column(columnDefinition = "text")
    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Column(nullable = false)
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "id=" + id +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
