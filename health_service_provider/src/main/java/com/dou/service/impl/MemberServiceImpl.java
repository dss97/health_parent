package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.MemberDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Member;
import com.dou.service.MemberService;
import com.dou.utils.MD5Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;
    //搜索会员信息
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //保存会员信息
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            //使用MD5将明文密码进行加密
            password=MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    //折线图
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCount=new ArrayList<>();
        for (String month : months) {
            String date=month+".31";
           Integer count= memberDao.findMemberCountBeforeDate(date);
           memberCount.add(count);
        }
    return  memberCount;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Member> page=memberDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
