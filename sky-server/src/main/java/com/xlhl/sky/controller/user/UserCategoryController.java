package com.xlhl.sky.controller.user;

import com.xlhl.sky.entity.Category;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.admin.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
public class UserCategoryController {

    @Resource(name = "categoryServiceImpl")
    private CategoryService categoryService;

    /**
     * 查询分类
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询分类")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
