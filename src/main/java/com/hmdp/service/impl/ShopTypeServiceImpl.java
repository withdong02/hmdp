package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.hmdp.utils.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从redis查询店铺类型
     * @return 店铺类型列表
     */
    @Override
    public Result queryList() {
        String key = RedisConstants.CACHE_SHOP_TYPE_KEY;
        List<String> listStr = stringRedisTemplate.opsForList().range(key, 0, -1);
        if (listStr != null && !listStr.isEmpty()) {
            List<ShopType> shopTypeList = listStr.stream()
                    .map((str) -> JSONUtil.toBean(str, ShopType.class))
                    .toList();
            return Result.ok(shopTypeList);
        }
        List<ShopType> list = this.query().orderByAsc("sort").list();
        list.forEach(shopType -> stringRedisTemplate.opsForList()
                .rightPush(key, JSONUtil.toJsonStr(shopType)));
        return Result.ok(list);
    }
}
