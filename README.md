# Widok skeleton
Install prerequisites:

```bash
git clone https://github.com/widok/widok.git
cd widok
sbt publish-local
cd ..
```

Clone repository:

```bash
git clone git@github.com:widok/skeleton.git
cd skeleton
```

When deploying, set the following environment variable:

```bash
export DEPLOY=true
```

This enables optimisations and runs the server on port 80.

Compile assets:

```bash
sbt assets
```

Compile to JavaScript and start server:

```bash
sbt ~reStart
```

## See also
* [https://www.typesafe.com/activator/template/widok-skeleton](Typesafe activator)
