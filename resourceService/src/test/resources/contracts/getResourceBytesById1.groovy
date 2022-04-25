package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return bytes by id=1'

    request {
        url 'resources/1'
        method GET()
    }

    response {
        status OK()
        body(
                fileAsBytes('sample-3s.mp3')
        )
    }
}