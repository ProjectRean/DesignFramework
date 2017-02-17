# DesignFramework
[![Build Status](https://travis-ci.org/kunonx/DesignFramework.svg?branch=master)](https://travis-ci.org/kunonx/DesignFramework) ![Version](https://img.shields.io/badge/version-1.2--RELEASE-yellowgreen.svg) [![License](https://img.shields.io/badge/LICENSE-MIT-blue.svg)](https://github.com/kunonx/DesignFramework/blob/master/LICENSE) ![Depend](https://img.shields.io/david/strongloop/express.svg)

DesignFramework is a collection of APIs that help you extend functionality when developing Minecraft plugin.
Key features currently available include simplified Command registration, custom SendMessage, and custom Entity Generation.
For more information, see the project's wiki.
# Recommand
This is my first plugin and last API collection. Therefore, here are many unstable elements and incomplete parts.
If you want to use it to do professional work, it's not RECOMMAND. The plugin was created for Java learning purposes and never developed for productivity.

# How can I use it?
If you are using Maven, you can write the Source below in pom.xml. 
Delete &lt;repositories&gt;, &lt;dependencies&gt; unless you haven't other package.

     <repositories>
        <repository>
            <id>kunonx-repo</id>
            <url>https://dev.kunonx.com/nexus/content/repositories/kunonx-repo</url>
        </repository>
    </repositories>
    </dependencies>
        <dependency>
            <groupId>io.github.kunonx</groupId>
            <artifactId>DesignFramework</artifactId>
            <version>1.1</version>
            <classifier>RELEASED</classifier>
        </dependency>
    </dependencies>
    
If you write using character UTF-8:

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

If you use the IDE without using it, add the compiled JAR file as an external library.
In addition to this, you can also use the uploaded source file directly.

# Can I editing and redistributing it?
DesignFramework is released under the MIT Lisense. According to the license, it can be handled by anyone free of charge.
However, you must include the copyright notice and this permission mark on all reproductions or important parts.
In addition, the author or copyright holder is not responsible for the software. Please refer to the license file.

For more information: https://opensource.org/licenses/mit-license.php

# Is there any reference to an external library for the project?
Please refer to the following for the external library. I did not refer to other parts.

- ParticleEffect: See source code comments
- FancyMessage: Copyright (c) 2013-2015 Max Kreminski
- GSON
- JSON

# What if I found a bug or want to support it?
Please use the issue tracker in Github, or contact me by email.

# Offical websites
- DesignFramework website: https://kunonx.com
- DesignFramework documention: https://dev.kunonx.com/docs/DesignFramework (offical, but not complete)
- DesignFramework for Jenkins: https://jenkins.kunonx.com/job/DesignFramework/

# Patch
- v1.2.0
NMS Package Reflection Added
Some independent APIs added

- v1.1.0
Fixed infinite constructor overloading bug
Fixed Korean(MS949) and Japanese typographical errors in LangConfiguration

- v1.0.2
Added language pack functionality
Added config editor

- v1.0.1
Improved recognition of LattCommand Arguments
Code improvements

- v1.0.0
First release
