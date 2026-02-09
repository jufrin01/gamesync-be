# 🎮 GameSync Backend API

Backend service untuk platform komunitas game **GameSync**. Dibangun menggunakan **Java Spring Boot**, **PostgreSQL**, dan **WebSocket** untuk fitur real-time chat.

## 🛠️ Teknologi yang Digunakan
* **Java 17 / 21**
* **Spring Boot 3** (Web, Data JPA, Security, WebSocket, Validation)
* **PostgreSQL** (Database)
* **JWT (JSON Web Token)** (Authentication)
* **Swagger UI (OpenAPI 3)** (API Documentation)
* **Lombok** (Boilerplate code reduction)

---

## ⚙️ Persiapan & Instalasi

### 1. Prerequisites
Pastikan kamu sudah menginstall:
* Java JDK 17 atau terbaru.
* Maven.
* PostgreSQL.

### 2. Konfigurasi Database
Buat database kosong di PostgreSQL bernama `gamesync`.
```sql

CREATE DATABASE gamesync;

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/gamesync
spring.datasource.username=postgres
spring.datasource.password=password_postgres_kamu

# Hibernate (Auto Create Tables)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration (Ganti dengan string acak yang panjang!)
gamesync.app.jwtSecret=RahasiaNegaraYangSangatPanjangDanAmanSekali123456789
gamesync.app.jwtExpirationMs=86400000

# Server Port
server.port=8080

mvn spring-boot:run

Jika sukses, akan muncul log: Started GamesyncApplication in ... seconds.

📚 Dokumentasi API (Swagger UI)
Kamu tidak perlu menebak-nebak endpoint! Dokumentasi lengkap dan fitur tes API tersedia otomatis.

Jalankan aplikasi.

Buka browser: http://localhost:8080/swagger-ui/index.html

Login dulu lewat endpoint /api/auth/signin untuk mendapatkan Token.

Klik tombol Authorize (gembok hijau), masukkan format: Bearer <Token_JWT_Kamu>.

🛣️ Daftar Endpoint Utama
🔐 Authentication (/api/auth)
POST /signup : Daftar akun baru.

POST /signin : Login (Dapat JWT Token).

👤 User Profile (/api/users)
GET /me : Lihat profil sendiri.

GET /me/stats : Lihat statistik RPG (Level, XP).

🛡️ Guilds (/api/guilds)
GET / : Lihat semua guild.

POST / : Buat guild baru (Otomatis jadi Leader).

POST /{id}/join : Gabung ke guild.

💬 Chat History (/api/chat)
GET /history : Ambil history chat global.

GET /history?guildId=1 : Ambil history chat khusus guild ID 1.

🔄 Flow Chat Real-time (WebSocket)
Fitur chat GameSync menggunakan protokol WebSocket (STOMP) agar pesan muncul secara instan tanpa perlu refresh halaman.

1. Koneksi (Handshake)
Frontend harus melakukan koneksi ke endpoint WebSocket:

URL: http://localhost:8080/ws

Protocol: SockJS & STOMP

2. Subscribe (Mendengarkan Pesan)
Setelah terkoneksi, client harus subscribe ke topic ini untuk menerima pesan orang lain:

Topic Public: /topic/public

3. Mengirim Pesan (Publish)
Untuk mengirim chat, kirim JSON ke destinasi berikut:

A. Join Room (Saat user pertama kali masuk halaman chat):

Destination: /app/chat.addUser

Payload:

JSON
{
    "sender": "Jufrin",
    "type": "JOIN"
}
B. Kirim Pesan Biasa:

Destination: /app/chat.sendMessage

Payload:

JSON
{
    "sender": "Jufrin",
    "content": "Halo semua, ayo mabar!",
    "type": "CHAT",
    "guildId": null  // null = Global, isi ID angka jika chat Guild
}
📝 Contoh Flow di Frontend (React/JS)
JavaScript
// 1. Koneksi
var socket = new SockJS('http://localhost:8080/ws');
stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    // 2. Subscribe
    stompClient.subscribe('/topic/public', function (payload) {
        var message = JSON.parse(payload.body);
        console.log("Pesan Masuk:", message);
        // Tampilkan pesan di layar
    });

    // 3. Register User ke Socket
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({
        sender: "UsernameKamu", 
        type: 'JOIN'
    }));
});

// 4. Kirim Pesan
function sendMessage() {
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
        sender: "UsernameKamu",
        content: "Hello World!",
        type: 'CHAT'
    }));
}
🤝 Kontribusi
Project ini dikembangkan oleh Jufrin sebagai Backend Developer.


### Tips Tambahan:
Jika kamu ingin file ini tampil rapi, buka file ini di GitHub atau Text Editor yang support Markdown Preview (seperti VS Code, tekan `Ctrl+Shift+V`).

Semoga membantu dokumentasi project kamu, Jufrin! Ada lagi yang bisa dibantu? 🚀