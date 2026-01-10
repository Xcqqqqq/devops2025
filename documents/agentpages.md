HomeView:

布局：
    顶部：用户头像、用户名、退出登录按钮
    左侧：导航栏（主页（默认），用户信息页等），更多栏目待实现
    中间：智能体列表（展示name、description）
        智能体布局：方块形，上方为avatar，下方为name、description，最下方为操作按钮（编辑、删除）
        添加智能体按钮：在最后一个智能体右方（或再下一行），加号图标

逻辑：
1. 登录成功后，去接口`GET /api/agent/list/{user}`获取该用户所有智能体，展示在HomeView中
2. 点击添加智能体按钮，弹窗输入智能体信息（name、description、prompt），检查用户permission是否为1，是则多加一个public字段输入框（下拉菜单，选项为是（1）/否（0））。用户在弹窗中点击确认按钮后，调用接口`POST /api/agent/create`创建智能体，参数为name、description、prompt、public（如果有）。返回ok后刷新HomeView。
3. 点击某个智能体的编辑按钮，先获取该智能体的信息（如果没有的话），之后弹窗输入智能体信息（description、prompt），name字段不可编辑，浅色显示。用户在弹窗中点击确认按钮后，调用接口`PUT /api/agent/update/{id}`更新智能体，参数为description、prompt。返回ok后刷新HomeView。
4. 点击某个智能体的删除按钮，弹窗确认删除，用户在弹窗中点击确认按钮后，调用接口`DELETE /api/agent/delete/{id}`删除智能体。返回ok后刷新HomeView。
5. 点击某个智能体的头像或名称，跳转到ChatView，展示该智能体的对话记录。