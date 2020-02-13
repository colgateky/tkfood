package com.tk.center;

import com.magic.springboot.listener.MultiListener;
import com.magic.springboot.service.MagicService;
import com.tk.center.entity.global.ClientSite;

public class MultiApplication extends CommonApplication{
    public static class ClientListener extends MultiListener {
        @Override
        protected MongoConfig getMongoConfig(String siteId) {
            MagicService magicService = globalMagicService;
            ClientSite site = magicService.get(siteId, ClientSite.class);
            if (site != null) {
                MongoConfig ret = new MongoConfig();
                ret.host = site.getMongoDB().getHost();
                ret.port = site.getMongoDB().getPort();
                ret.database = site.getMongoDB().getDatabase();
                ret.username = site.getMongoDB().getUsername();
                ret.password = site.getMongoDB().getPassword();
                return ret;
            }
            return null;
        }

        @Override
        protected RedisConfig getRedisConfig(String siteId) {
            MagicService magicService = globalMagicService;
            ClientSite site = magicService.get(siteId, ClientSite.class);
            if (site != null) {
                RedisConfig ret = new RedisConfig();
                ret.host = site.getRedisDB().getHost();
                ret.database = site.getRedisDB().getDatabase();
                ret.port = site.getRedisDB().getPort();
                ret.password = site.getRedisDB().getPassword();
                return ret;
            }
            return null;
        }

        @Override
        protected String getGameServerConfig(String clientId) {
            MagicService magicService = globalMagicService;
            ClientSite site = magicService.get(clientId, ClientSite.class);
            if (site != null){
                return site.getGameServer();
            }
            return null;
        }

        @Override
        protected String getGameClientConfig(String clientId) {
            MagicService magicService = globalMagicService;
            ClientSite site = magicService.get(clientId, ClientSite.class);
            if (site != null){
                return site.getGameClientId();
            }
            return null;
        }
    }

}
