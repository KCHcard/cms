package com.cms.service.impl;

import com.cms.entity.User;
import com.cms.mapper.UserMapper;
import com.cms.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Zhang MX
 * @since 2025-02-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
