# Bet Data Report Generator

A spring-boot application with Apache Camel that consumes a message from source URI, processes the message and sends a result to target URI. 

## Installation

Can be easily installed using ```mvn clean install```

## Usage

Just put a file named dataset.csv into ${java.io.tmpdir}. This location can be changed in ```com.bet.data.report.generator.Application``` by defining a ```sourceFrom``` in ```@SourceUri(sourceFrom =...)```

## Additional details
```@SourceUri(sourceFrom = "file:{{java.io.tmpdir}}?fileName=dataset.csv&autoCreate=false")``` can be changed to quartz2 endpoint to schedule the triggering of the Route. Then in ```com.bet.data.report.generator.builder.BettingDataReportRouteBuilder.enrichExchange``` an HTTP call can be configured.

In order to change the source and/or target message format, we will need to change the generic types ```S```, ```R1``` and ```R2``` in ```BettingDataReportRouteBuilder<S, R1, R2>```. For example ```com.bet.data.report.generator.Application``` has ```CsvDataset, SelectionLiability, TotalLiability```. Now if we want to use Json as a source we would need to change ```com.bet.data.report.generator.model.CsvDataset``` to ```org.apache.camel.util.json.JsonArray```.

Similarly the target message format can be changed by changing ```@TargetUri(targetTo = "stream:out")```. ```targetTo``` can be changed to file endpoint, meaning that the file with a message will be created.
