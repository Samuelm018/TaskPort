-- Schema for company database (creates client, projects, messages). Do NOT create admin table here.
-- Schema for company database (creates admin, client, projects, messages)

CREATE TABLE IF NOT EXISTS admin (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  gmail VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS client (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  gmail VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS projects (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  cost_min DECIMAL(12,2),
  cost_max DECIMAL(12,2),
  deadline DATE,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  project_id BIGINT NOT NULL,
  client_id BIGINT NOT NULL,
  message TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- Note: admin user will be seeded on application startup by DataInitializer.
