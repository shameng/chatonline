package com.meng.chatonline.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author xindemeng
 *
 * 身份授予实体类
 */
@Entity
@Table(name="user_runAs")
public class UserRunAs implements Serializable
{
    private static final long serialVersionUID = -1981669260628008092L;

    private Integer id;
    //授予身份者
    private User fromUser;
    //被授予身份者
    private User toUser;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @JoinColumn(name = "from_user_id", nullable = false)
    @ManyToOne
    public User getFromUser()
    {
        return fromUser;
    }

    public void setFromUser(User fromUser)
    {
        this.fromUser = fromUser;
    }

    @JoinColumn(name = "to_user_id", nullable = false)
    @ManyToOne
    public User getToUser()
    {
        return toUser;
    }

    public void setToUser(User toUser)
    {
        this.toUser = toUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserRunAs userRunAs = (UserRunAs) o;

        if (fromUser != null ? !fromUser.getId().equals(userRunAs.fromUser.getId()) : userRunAs.fromUser.getId() != null)
            return false;
        if (toUser != null ? !toUser.getId().equals(userRunAs.toUser.getId()) : userRunAs.toUser.getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromUser.getId() != null ? fromUser.getId().hashCode() : 0;
        result = 31 * result + (toUser.getId() != null ? toUser.getId().hashCode() : 0);
        return result;
    }
}
