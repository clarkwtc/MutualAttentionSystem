package server

func main() {
    route := SetupRouter()
    err := route.Run(":8080")
    if err != nil {
        return
    }
}
