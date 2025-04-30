const dbName = "management-tool";

// Get database instances
const adminDB = db.getSiblingDB("admin");
const databases = adminDB.runCommand({ listDatabases: 1 }).databases;

// To recreate in the Docker start
const dbExists = databases.some(database => database.name === dbName);
if (dbExists) {
    print(`Database '${dbName}' exists. Droping it...`);
    db.getSiblingDB(dbName).dropDatabase();
} else {
    print(`Database '${dbName}' does not exist. Creating it...`);
}

// Init users ID=1
const myDb = db.getSiblingDB(dbName);
myDb.users.insertOne({
    id: "1",
    firstName: "Sly",
    lastName: "Cooper",
    nickname: "SlyCooper2025",
    password: "password",
    email: "sly@cooper.com",
    country: "UK",
    created_at: "2025-01-01T07:20:50.52Z",
    updated_at: "2025-10-12T07:20:50.52Z"
});
var uniqueUser = myDb.users.find().toArray()
print(`Database '${dbName}' init done.`);
printjson(uniqueUser);
print(`mongo-init.js script ends`);