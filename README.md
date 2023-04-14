# 项目工程介绍
2022/4/15

**项目名**：自适应学习系统

# 项目特点
结合目前国内在线课程平台的基础功能，通过知识点图谱进行个性化推荐学习

# 项目的基本结构
Rec-platform：下面的模块依赖上面的模板
1. platform-core        -- 工具类和通用类
2. platform-user        -- 用户模块
3. platform-points      -- 知识点模块
4. platform-resources   -- 资源模块
5. platform-course      -- 课程模块
6. platform-exam        -- 习题测试模块
7. platform-statistics  -- 统计分析模块
8. platform-blog        -- 博客模块
9. platform-function    -- 通知模块
10. platform-system     -- 系统模块（运行）

# 使用方法
1. 修改MySQL配置
2. 改Redis配置
3. 修改阿里云oss和阿里云视频点播配置

# 代码规范
1. 使用DTO接收前端传来的数据，VO返回前端需要数据
2. 统一使用ResultResponse来返回结果
3. 异常信息统一用PlatformException来抛出
4. 新实体类都需要基础BaseEntity类
5. 不用QueryWrapper，用LambdaQueryWrapper
6. 代码遵循Restful风格

# 相关链接
1. ApiFox接口文档：https://www.apifox.cn/web/project/792185
2. coding仓库：https://rec-platform.coding.net/p/pt

# 参与贡献
1.  姚俊杰
2.  林伟杰
3.  洪恩杰
4.  苏思楠
5.  吴钿沣
6.  陈建誉

# 其他
不符合三范式地方：
课时里包含了作业id和作业名
用户知识点中间表，包含了知识点名
