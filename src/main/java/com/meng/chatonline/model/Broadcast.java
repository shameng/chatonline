package com.meng.chatonline.model;

import com.meng.chatonline.Param;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author xindemeng
 */
@Table(name = "broadcast")
@Entity
public class Broadcast implements Serializable
{
    private Integer id;
    //发表人
    private User utterer;
    @Length(min = 1, max = 50)
    private String title;
    @NotEmpty
    private String content;
    private Date date;

    //广播类型，0-公告，1-登陆，2-注销。不持久化
    private Integer type = Param.NOTICE_BROADCAST_TYPE;

    @Transient
    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @JoinColumn(name="utterer_id", nullable = false)
    @ManyToOne
    public User getUtterer()
    {
        return utterer;
    }

    public void setUtterer(User utterer)
    {
        this.utterer = utterer;
    }

    @Column(nullable = false)
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Column(nullable = false, columnDefinition = "text")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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
}
