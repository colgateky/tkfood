package com.tk.center.admin.controller.memeber;

import com.tk.center.admin.controller.BaseController;
import com.tk.center.dto.member.MemberLevelQuery;
import com.tk.center.entity.member.MemberLevel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Mingkun on 2020/02/04.
 */
@RestController
@RequestMapping("/admin/member_level")
public class MemberLevelController extends BaseController<MemberLevel, MemberLevelQuery> {
    @Override
    protected Class<MemberLevel> getModelCls() {
        return MemberLevel.class;
    }

    @Override
    protected void onBeforePersist(MemberLevel memberLevel) {
        super.onBeforePersist(memberLevel);
        memberLevel.setId("Level" + memberLevel.getLevel());
    }
}
