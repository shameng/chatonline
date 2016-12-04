package com.meng.chatonline.controller;

import com.meng.chatonline.model.Broadcast;
import com.meng.chatonline.model.User;
import com.meng.chatonline.service.BroadcastService;
import com.meng.chatonline.websocket.MyWebSocketHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;

/**
 * @Author xindemeng
 */
@Controller
@RequestMapping("/broadcast")
public class BroadcastController
{
    @Resource
    private BroadcastService broadcastService;
    @Resource
    private MyWebSocketHandler webSocketHandler;

    @RequestMapping({"","/"})
    public String broadcast(Map<String, Object> map)
    {
        List<Broadcast> broadcastList = broadcastService.getBroadcastList();
        map.put("broadcastList", broadcastList);
        return "broadcast";
    }

    @RequestMapping("/newBroadcast")
    public String newBroadcast(Map<String, Object> map)
    {
        map.put("broadcast", new Broadcast());
        return "editBroadcast";
    }

    @RequestMapping(value="/newBroadcast", method = RequestMethod.POST)
    public String newBroadcast(@Valid Broadcast broadcast, BindingResult result, HttpSession session,
                               Map<String, Object> map)
    {
        //如果有不合要求的输入
        if (result.getFieldErrorCount() > 0)
        {
//            System.out.println("出错了!");
//            for (FieldError error : result.getFieldErrors())
//            {
//                System.out.println(error.getField() + ":" + error.getDefaultMessage());
//            }
            List<Broadcast> broadcastList = broadcastService.getBroadcastList();
            map.put("broadcastList", broadcastList);
            return "editBroadcast";
        }

        User user = (User) session.getAttribute("user");
        broadcast.setUtterer(user);
        broadcastService.saveBroadcast(broadcast);

        //广播公告
        webSocketHandler.broadcast(broadcast);

        return "redirect:/broadcast";
    }

    @ResponseBody
    @RequestMapping("/deleteBroadcast")
    public String deleteBroadcast(@RequestParam(value = "id",required = true) String id)
    {
        try
        {
            this.broadcastService.deleteBroadcast(Integer.parseInt(id));
            return "1";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "0";
        }
    }

}
