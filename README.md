# EcosedDroid
EcosedDroid

EcosedDroid是一款Android高级开发框架,利用Kotlin语法提供类似基础组件封装的功能,但不需要继承,只需要附加即可,包含沉浸式适配,对Shizuku,Taskbar等第三方框架的再封装,并提供了一些诸如提示,延时操作,日志输出等小工具,以方便快速构建应用程序.

[//]: # (如何使用)

[//]: # ()
[//]: # (1.在项目根目录的settings.gradle.kts中添加Jitpack仓库)

[//]: # ()
[//]: # (repositories {  )

[//]: # (----maven&#40;url = "https://jitpack.io"&#41;  )

[//]: # (})

[//]: # ()
[//]: # (2.在要使用的module的build.gradle.kts中添加EcosedPlugin和LibEcosed依赖&#40;注意: LibEcosed使用了RikkaX AppCompat, 直接引入会与AndroidX AppCompat冲突, 需要手动exclude屏蔽&#41;)

[//]: # ()
[//]: # (dependencies {  )

[//]: # (----implementation&#40;dependencyNotation = "com.github.ecosed:plugin:[![]&#40;https://jitpack.io/v/ecosed/plugin.svg&#41;]&#40;https://jitpack.io/#ecosed/plugin&#41;"&#41;  )

[//]: # (----implementation&#40;  )

[//]: # (--------dependencyNotation = "com.github.ecosed:libecosed:[![]&#40;https://jitpack.io/v/ecosed/libecosed.svg&#41;]&#40;https://jitpack.io/#ecosed/libecosed&#41;",  )

[//]: # (--------dependencyConfiguration = {  )

[//]: # (------------exclude&#40;  )

[//]: # (----------------group = "dev.rikka.rikkax.appcompat",  )

[//]: # (----------------module = "appcompat"  )

[//]: # (------------&#41;  )

[//]: # (--------}  )

[//]: # (----&#41;  )

[//]: # (})

[//]: # ()
[//]: # (3.点击SyncNow完成工程设定.)

[//]: # ()
[//]: # (4.配置Application全局类, 点击查看[示例代码]&#40;https://github.dev/ecosed/libecosed/blob/master/demo/src/main/kotlin/io/ecosed/libecosed_example/MyApplication.kt&#41;.)