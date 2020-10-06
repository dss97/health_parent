package com.dou.service;

import com.dou.pojo.Member;

import java.util.List;

public interface MemberService {
    /**
     * 搜索是否是会员
     * 返回会员信息
     * @param telephone
     * @return
     */
    public Member findByTelephone(String telephone);

    /**
     * 如果不是会员,根据手机号进行添加
     * @param member
     */
    public void add(Member member);
    public List<Integer> findMemberCountByMonths(List<String> months);
}
