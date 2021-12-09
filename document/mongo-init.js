db.createUser(
        {
            user: "toys-user",
            pwd: "root",
            roles: [
                {
                    role: "readWrite",
                    db: "toys-db"
                }
            ]
        }
);