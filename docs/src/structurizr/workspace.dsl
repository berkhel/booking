workspace "Booking" "Sample app for booking ticket"

    !identifiers hierarchical

    model {
        u = person "User"
        ss = softwareSystem "Booking System" {
            wa = container "Application"
            db = container "Relational Database" {
                technology "MySQL Server"
                description "Stores events and relative remaining tickets\n Stores successful bookings"
                tags "Database"
            }
        }

        u -> ss.wa "Send Booking Request" "HTTP"
        ss.wa -> ss.db "Reads remaining tickets from\n --------------------- \n Writes bookings to" "JDBC"
    }

    views {
        systemContext ss "Diagram1" {
            include *
            autolayout lr
        }

        container ss "Diagram2" {
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
        }
    }
}