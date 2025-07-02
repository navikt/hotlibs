package no.nav.hjelpemidler.streams

import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import java.util.Properties

fun topology(block: StreamsBuilder.() -> Unit): Topology =
    StreamsBuilder().apply(block).build()

fun kafkaStreams(
    applicationId: String,
    configuration: Properties = kafkaStreamsConfiguration(applicationId),
    builder: StreamsBuilder,
): KafkaStreams = KafkaStreams(builder.build(), configuration)

fun kafkaStreams(
    applicationId: String,
    configuration: Properties = kafkaStreamsConfiguration(applicationId),
    block: StreamsBuilder.() -> Unit,
): KafkaStreams = kafkaStreams(applicationId, configuration, builder = StreamsBuilder().apply(block))
