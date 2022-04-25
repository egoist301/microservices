package contract

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should create song and return id"

    request {
        url "/songs"
        method POST()
        headers {
            contentType applicationJson()
        }
        body("name": "We Are the Champions",
                "artist": "Queen",
                "album": "News of the World",
                "length": "3:14",
                "resourceId": 7,
                "year": 1977)
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body("id": "1")
    }
}
