package com.meng.chatonline.controller.oauth2;

import com.meng.chatonline.model.oauth2.Client;
import com.meng.chatonline.service.oauth2.ClientService;
import com.meng.chatonline.service.oauth2.impl.ClientServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xindemeng
 */
@Controller
@RequestMapping("/client")
public class ClientController
{
    @Resource(name = "clientService")
    private ClientService clientService;

    @RequiresPermissions("client:query")
    @RequestMapping({"", "/"})
    public String clientList(Model model)
    {
        List<Client> clients = this.clientService.findAllEntities();
        model.addAttribute("clients", clients);
        return "oauth2/clientList";
    }

    @RequiresPermissions("client:create")
    @ResponseBody
    @RequestMapping(value="/newClient", method = RequestMethod.POST)
    public Object newClient(@RequestParam(name = "clientName") String clientName)
    {
        try
        {
            Client client = new Client();
            client.setClientName(clientName);
            Client newClient = this.clientService.createClient(client);
            return newClient;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresPermissions("client:update")
    @ResponseBody
    @RequestMapping(value="/editClient", method = RequestMethod.POST)
    public String editClient(@RequestParam("cId") Long cId,
                             @RequestParam("clientName") String clientName)
    {
        try
        {
            this.clientService.updateClientName(cId, clientName);
            return "1";
        }catch (Exception e)
        {
            e.printStackTrace();
            return "0";
        }
    }

    @RequiresPermissions("client:delete")
    @ResponseBody
    @RequestMapping(value = "/deleteClient", method = RequestMethod.POST)
    public String deleteClient(@RequestParam("cId") Long cId)
    {
        try
        {
            this.clientService.deleteClient(cId);
            return "1";
        }catch (Exception e)
        {
            e.printStackTrace();
            return "0";
        }
    }
}
