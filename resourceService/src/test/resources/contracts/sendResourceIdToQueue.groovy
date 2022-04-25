package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should send 1 to queue'
    label 'resource-created'

    input {
        triggeredBy('sendResourceIdToQueue()')
    }

    outputMessage {
        sentTo 'resource-created-exchange'
        body(1L)
    }
}