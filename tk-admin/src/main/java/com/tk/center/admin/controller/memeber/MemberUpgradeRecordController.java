package com.tk.center.admin.controller.memeber;

import com.tk.center.admin.controller.QueryController;
import com.tk.center.dto.record.MemberUpgradeRecordQuery;
import com.tk.center.entity.record.MemberUpgradeRecord;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Mingkun on 2020/02/04.
 */
@RestController
@RequestMapping("/admin/member_upgrade_record")
public class MemberUpgradeRecordController extends QueryController<MemberUpgradeRecord, MemberUpgradeRecordQuery> {
    @Override
    protected Class<MemberUpgradeRecord> getModelCls() {
        return MemberUpgradeRecord.class;
    }

    @Override
    protected void onAfterPage(Page<MemberUpgradeRecord> page) {
        super.onAfterPage(page);
    }
}
