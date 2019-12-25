import org.scalatest.{AsyncFunSpec, AsyncFunSuite, Matchers}

class InputStreamTest extends AsyncFunSpec with Matchers {

  describe("mouse") {
      it("must allow me to pop") {
        HelloWorld.sayHelloTo("Harry") map { result =>
          result shouldBe Some("Hello Harry, welcome to the future world!")
        }
      }
  }
}

/*
    assert(sum == 3)
  }


  test("throw error if invalid csv file") {
    assert(9 == 3)
  }

  test("throw error if file not present") {
    assert(9 == 3)
  }

 */