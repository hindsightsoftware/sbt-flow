# sbt-flow [![Circle CI](https://circleci.com/gh/hindsightsoftware/sbt-flow/tree/master.svg?style=svg)](https://circleci.com/gh/hindsightsoftware/sbt-flow/tree/master)  [ ![Download](https://api.bintray.com/packages/hindsightsoftware/sbt-plugins/sbt-flow/images/download.svg) ](https://bintray.com/hindsightsoftware/sbt-plugins/sbt-flow/_latestVersion)

An SBT plugin for performing static type checking of JavaScript using [Flow](http://flowtype.org/). Implemented as a
[sbt-web](https://github.com/sbt/sbt-web) plugin and is aware of the asset pipeline and uses this as the default
source for JavaScript files.

## Setup


This plugin does not install `flow` and requires the application to be installed and available in your `PATH`,
see [Installing Flow](http://flowtype.org/docs/getting-started.html).

To use this plugin use the addSbtPlugin command within your project's `plugins.sbt` file:

    resolvers += Resolver.url("hindsightsoftware-sbt-plugin-releases",
          url("http://dl.bintray.com/hindsightsoftware/sbt-plugins"))(
          Resolver.ivyStylePatterns)

    addSbtPlugin("com.hindsightsoftware.sbt" % "sbt-flow" % "0.1-dc8856c6e5a0adfa4562520d6f98194fb327d67e")


Your project's build file also needs to enable sbt-web plugins. For example with build.sbt:

    lazy val root = (project in file(".")).enablePlugins(SbtWeb)

## Usage

### init
To set up a new project using Flow, all you need to do is initialize your root JavaScript folder tell Flow to start
typechecking files within your project. `> flow::init` will initializes the `sourceDirectory in Assets` to be used as
the root directory.

```
> flow::init
```

### Check

Typecheck JavaScript files using Flow without starting the Flow server. Equivalent to `$ flow check`

```
> flow::check
```

## Options

The Flow can be configured in `build.sbt` with these properties

```
FlowKeys.allFiles := true
```

#### FlowKeys.allFiles
Typecheck all files, not just files containing the `@flow` annotation.

Type: `Boolean`
Default: `false`

#### FlowKeys.weakInference

Typecheck with weak inference, assuming dynamic types by default

Type: `Boolean`
Default: `false`

#### FlowKeys.interfacePaths
Specify one or more library paths for interfaces and declaration

Type: `Seq[String]`
Default: `Seq.empty`



&copy; [Hindsight Software Ltd](http://hindsightsoftware.com), 2015