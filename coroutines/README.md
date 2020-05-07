# Coroutines

Some experiments

### [Coroutines.kt](src/main/kotlin/org/jan/Coroutines.kt)

```$xslt
[L8:T0ms:mainThread]	 Start execution
[L17:T147ms:mainThread]	 suspend main function delay 2 seconds coroutine
[L12:T150ms:DefaultDispatcher-worker-1Thread]	 Global Scope launch coroutine delay 1 second
[L14:T1163ms:DefaultDispatcher-worker-1Thread]	 Hello
[L21:T2165ms:kotlinx.coroutines.DefaultExecutorThread]	 Stop execution
```


### [CoroutineScope.kt](src/main/kotlin/org/jan/CoroutineScope.kt)

```$xslt
[L22:T1ms:mainThread]	 Task from coroutineScope - context: [ScopeCoroutine{Active}@51521cc1, BlockingEventLoop@1b4fb997]
[L12:T106ms:mainThread]	 Launched task from runBlocking - context: [StandaloneCoroutine{Active}@593634ad, BlockingEventLoop@1b4fb997]
[L18:T806ms:mainThread]	 Launch task from coroutineScope - context: [StandaloneCoroutine{Active}@20fa23c1, BlockingEventLoop@1b4fb997]
[L25:T807ms:mainThread]	 runBlocking after coroutineScope ended - context: [BlockingCoroutine{Active}@3581c5f3, BlockingEventLoop@1b4fb997]
```

### [ThreadsVsCoroutines.kt](src/main/kotlin/org/jan/ThreadsVsCoroutines.kt)


```$xslt
[L8:T1ms:mainThread]	 Launch 1 Million Coroutines
[L17:T1319ms:mainThread]	 launch finishes faster but does not synchronize
[L18:T1319ms:mainThread]	 Total 369053467146
[L19:T1319ms:mainThread]	 -----------------------------------------------
[L21:T1320ms:mainThread]	 Launch 1 Million Coroutines with async and await
[L30:T63410ms:mainThread]	 Await finishes as slower as threads but with correct values
[L31:T63411ms:mainThread]	 Total 500000500000
[L34:T63411ms:mainThread]	 -----------------------------------------------
[L35:T63411ms:mainThread]	 Start 1 Million Threads
[L43:T113713ms:mainThread]	 Total 500000500000
```