*Setting up spark*

- Have a local spark 2.4.7 installation
- Create a `spark-defaults.conf` configuration file under `$SPARK_HOME/conf`. Please look at `spark-default.conf` in this directory for an example configuration (and make sure to change the Crail related paths accordingly).
- Clone this repository https://github.com/IBM/spark-tpc-ds-performance-test which will be used to generate a TPC-DS dataset
- `cd` into `spark-tpc-ds-performance-test` and set `SPARK_HOME` variable in `spark-tpc-ds-performance-test/bin/tpcdsenv.sh` to the path of your spark installation
- Now run `bin/tpcdsspark.sh` and choose "`Create spark tables`". After a while this should have created a directory called `spark-warehouse` within your `$SPARK_HOME` containing a `tpcsds.db` file. This file will be used for the TPC-DS queries.

*Setting up crail*
- To reproduce the issue, a single datanode should be sufficient. Start a Crail namenode and Crail datanode locally.
- Please look at `crail-site.conf` in this directory for an example configuration.

*Running the workload*

- Run the following command to start the workload on spark: `$SPARK_HOME/bin/spark-submit <path/to/workload/jar>/workload.jar 10`
- The jar containing the workload application is also part of this folder. `10` simply denotes the number of the TPC-DS query which should be used.

After some time these steps should then result in the workload application to crash with an exception like this: 
```
java.util.concurrent.ExecutionException: java.util.concurrent.ExecutionException: java.lang.IllegalArgumentException
        at org.apache.crail.utils.MultiFuture.get(MultiFuture.java:93)
        at org.apache.crail.CrailBufferedInputStream.getSlice(CrailBufferedInputStream.java:430)
        at org.apache.crail.CrailBufferedInputStream.read(CrailBufferedInputStream.java:129)
        at java.io.BufferedInputStream.fill(BufferedInputStream.java:246)
        at java.io.BufferedInputStream.read(BufferedInputStream.java:265)
        at java.io.DataInputStream.readInt(DataInputStream.java:387)
        at org.apache.spark.sql.execution.UnsafeRowSerializerInstance$$anon$2$$anon$3.readSize(UnsafeRowSerializer.scala:113)

        ...
```

Sometimes the Crail datanode also crashes before:
```
20/10/26 08:20:09 INFO crail: datanode statistics, freeBlocks 1000637
java.nio.BufferOverflowException
	at java.nio.HeapByteBuffer.put(HeapByteBuffer.java:222)
	at org.apache.crail.storage.tcp.TcpStorageResponse$ReadResponse.write(TcpStorageResponse.java:120)
	at org.apache.crail.storage.tcp.TcpStorageResponse.write(TcpStorageResponse.java:78)
	at com.ibm.narpc.NaRPCProtocol.makeMessage(NaRPCProtocol.java:15)
	at com.ibm.narpc.NaRPCServerChannel.transmitMessage(NaRPCServerChannel.java:49)
	at com.ibm.narpc.NaRPCDispatcher.run(NaRPCDispatcher.java:87)
	at java.lang.Thread.run(Thread.java:748)
```

When changing the Crail configuration to use the RDMA storage tier this error should not be observable. Instead after a while the workload application should log that is successfully completed tasks like this:
```
Running with TPC_DS query10
Performing run 1 with 50 parallel tasks

Completed task 18 resulting in 100 rows
Completed task 5 resulting in 100 rows
Completed task 30 resulting in 100 rows
Completed task 23 resulting in 100 rows
Completed task 21 resulting in 100 rows
Completed task 11 resulting in 100 rows
Completed task 7 resulting in 100 rows
Completed task 9 resulting in 100 rows

...
```

