package com.tk.center.service;

import com.tk.center.entity.types.TerminalType;

public interface RealtimeStatService {
    //void onPlayerKeepAlive(String playerId);
    void onMemberRegister(String playerId, TerminalType type);
    //void onPlayerDeposit(String playerId);
    //void handle() throws Exception;

    //SysMessage getSysMessage();
    //void saveSysMessage(SysMessage msg);

}
