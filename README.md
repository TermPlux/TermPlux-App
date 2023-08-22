# libecosed
libecosed

[![](https://jitpack.io/v/ecosed/libecosed.svg)](https://jitpack.io/#ecosed/libecosed)

如何使用

`repositories { 
    maven(url = "https://jitpack.io")
}`

`dependencies {
    implementation(dependencyNotation = "com.github.ecosed:plugin:4.2.0")
    implementation(
        dependencyNotation = "com.github.ecosed:libecosed:0.0.0-pretest01",
        dependencyConfiguration = {
            exclude(
                group = "dev.rikka.rikkax.appcompat",
                module = "appcompat"
            )
        }
    )
}`