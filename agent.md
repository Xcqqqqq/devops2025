主页：HomeView 展示该用户所有智能体
按钮：创建智能体（弹窗输入，检查用户permission是否为1，是则多加一个public字段输入框）
接口：`POST /api/agent/create`
输入：
    String name
    String description
    String prompt
    Boolean public (不一定有，没有输入则默认0)
行为：创建对象保存到后端数据库agent表中（用id做key）
返回：ok


接口：`GET /api/agent/get/{id}`
行为：按id搜索数据库agent表中对象，返回对象
返回：ok
异常：找不到key


接口：`GET /api/agent/list/`
行为：返回数据库agent表中所有对象，按当前userId筛选
返回：ok
异常：找不到user


按钮：编辑智能体（弹窗输入）
接口：`PUT /api/agent/update/{id}`
输入：
    String description （页面显示）
    String prompt
行为：修改数据库agent表中的对象
返回：ok


按钮：删除智能体（弹窗确认，传id）
接口：`DELETE /api/agent/delete/{id}`
行为：删除数据库agent表中的对象
返回：ok
异常：找不到key

