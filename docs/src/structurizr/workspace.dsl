workspace "Name" "Description"

    !identifiers hierarchical

    model {

        u = person "User"
        ss = softwareSystem "Booking System" {
            wa = container "Application" {
                ctrl = component "Rest Controller" {
                        description "Receives REST API requests"
                        technology "Spring Web"
                        tag "Controller"
                }
                
                app =  component "App" {
                        description "Main application logic"
                        tag "App"
                }
                
                repo = component "JPA Repository" {
                        description "Handles JDBC queries"
                        technology "Spring Data JPA"
                        tag "Repository"
                    }
                ampq = component "Publisher" {
                        description "Push messages into the queue of the message broker"
                        technology "Spring AMPQ"
                        tag "AMPQ"
                    }
                
                config = component "Configurator" {
                        description "Instantiates the app and the other components"
                        technology "Spring Boot"
                        tag "Configurator"
                    }
                }
            db = container "Relational Database" {
                technology "MySQL Server"
                description "Stores events and relative remaining tickets\n Stores successful bookings"
                tags "Database"
            }
            mq = container "Message Broker" {
                technology "RabbitMQ"
                description "Send asynchronously message to the Email Server"
                tags "Queue"
            }
            email = container "Email Server" {
                technology "Spring AMPQ + SimpleJavaMail"
                description "Consumes messages and then send emails to User"
                tags "Email" 
            }
        }

        u -> ss.wa.ctrl "Send Booking Request" "HTTP"
        ss.wa.repo -> ss.db "Reads remaining tickets from\n --------------------- \n Writes bookings to" "JDBC"
        ss.wa.config -> ss.wa.repo "Instantiates"
        ss.wa.config -> ss.wa.app "Instanciates"
        ss.wa.config -> ss.wa.ctrl "Instantiates"
        ss.wa.config -> ss.wa.ampq "Instantiates"
        ss.wa.ctrl -> ss.wa.app "Uses"
        ss.wa.app -> ss.wa.repo "Uses"
        ss.wa.app -> ss.wa.ampq "Uses"
        ss.wa.ampq -> ss.mq "Push Messages"
        ss.email -> ss.mq "Consumes"
        ss.email -> u "Notify" "SMTP"
        
    }

    views {
        systemContext ss "BookingSystem" {
            include *
            autolayout lr
        }
        container ss "BookingContainer" {
            include *
            autolayout lr
        }
        component ss.wa "BookingApp" {
            include ss.email
            include *
            autolayout lr
        }
        
        styles { 
            element "Element" {
                color white
            }
            element "Person" {
                background #08427b
                shape person
            }
            element "Software System" {
                background #2D882D
            }
            element "Container" {
                background #55aa55
            }
            element "Database" {
                shape cylinder
            }
            element "Queue" {
                shape pipe
            }
            element "Controller" {
                background #71ce81
                shape component
                color black 
                icon ./images/spring-boot-logo.png
            } 
            element "App" {
                background #71ce81
                shape hexagon
                color black 
            }
            element "Repository" {
                background #71ce81
                shape component
                color black
                icon ./images/spring-boot-logo.png
            } 
            element "AMPQ" {
                background #71ce81
                shape component
                color black
                icon ./images/spring-boot-logo.png
            } 
            element "Configurator" {
                background #71ce81
                color black
                shape roundedbox
                icon ./images/spring-boot-logo.png
            }
    }

    }
    
    
   
}
