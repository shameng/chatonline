package com.meng.chatonline.model.oauth2;

import javax.persistence.*;

/**
 * @author xindemeng
 *
 * 客户端或者第三方应用信息实体类
 */
@Table(name="oauth2_clients")
@Entity
public class Client
{
    private Long id;
    //客户端名称
    private String clientName;
    //客户端id
    private String clientId;
    //客户端安全key
    private String clientSecret;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "client_name", nullable = false)
    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }

    @Column(name = "client_id", nullable = false)
    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    @Column(name = "client_secret", nullable = false)
    public String getClientSecret()
    {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return id != null ? id.equals(client.id) : client.id == null;

    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }
}
