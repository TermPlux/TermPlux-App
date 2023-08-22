# LibEcosed
LibEcosed

如何使用

repositories {
----maven(url = "https://jitpack.io")
}

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