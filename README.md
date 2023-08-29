# LibEcosed
LibEcosed

如何使用

1.在项目根目录的settings.gradle.kts中添加Jitpack仓库

repositories {  
----maven(url = "https://jitpack.io")  
}

2.在要使用的module的build.gradle.kts中添加EcosedPlugin和LibEcosed依赖(注意: LibEcosed使用了RikkaX AppCompat, 直接引入会与AndroidX AppCompat冲突, 需要手动exclude屏蔽)

dependencies {  
----implementation(dependencyNotation = "com.github.ecosed:plugin:[![](https://jitpack.io/v/ecosed/plugin.svg)](https://jitpack.io/#ecosed/plugin)")  
----implementation(  
--------dependencyNotation = "com.github.ecosed:libecosed:[![](https://jitpack.io/v/ecosed/libecosed.svg)](https://jitpack.io/#ecosed/libecosed)",  
--------dependencyConfiguration = {  
------------exclude(  
----------------group = "dev.rikka.rikkax.appcompat",  
----------------module = "appcompat"  
------------)  
--------}  
----)  
}

3.点击SyncNow完成工程设定.

4.配置Application全局类, 点击查看[示例代码](https://github.dev/ecosed/libecosed/blob/master/demo/src/main/kotlin/io/ecosed/libecosed_example/MyApplication.kt).