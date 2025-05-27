-- src/main/resources/import.sql

-- Insert sample Institutions
INSERT INTO institution (id, name, address, email, phone) VALUES (1, 'Universidade PUC', 'Rua Teste, 123', 'contato@puc.edu', '1111-1111');
INSERT INTO institution (id, name, address, email, phone) VALUES (2, 'Outra Faculdade', 'Avenida Exemplo, 456', 'info@outrafaculdade.com', '2222-2222');

-- Insert sample encoded password for 'password' (example BCrypt hash)
-- You should generate your own hashes for production
-- Hash for 'password' using BCrypt: $2a$10$variouscharacters......................
-- Using a placeholder hash for 'password'
-- You can generate one using a tool or a small Spring Security script
-- For example, using BCryptPasswordEncoder().encode("password")
-- Placeholder hash for 'password':
-- $2a$10$8.CQVn12q7/aB3Y9wFjS/.E.1gSg0f5kLgO/4Xb8zW7rN4mY5pPru

-- Note: IDs are explicitly set here. In a real scenario with auto-increment, you might omit IDs
-- and let the database generate them, but for import.sql with relationships, fixing IDs can simplify.

-- Insert sample Professor (linked to Institution 1)
-- Password 'password' encoded
INSERT INTO professor (id, name, cpf, department, institution_id, email, password, coin_balance) VALUES (1, 'Professor Exemplo', '11122233344', 'Departamento de TI', 1, 'professor.exemplo@puc.edu', '$2a$10$variouscharacters......................', 1000.00);

-- Insert sample Student (linked to Institution 1)
-- Password 'password' encoded
INSERT INTO student (id, name, email, cpf, rg, address, institution_id, course, password, coin_balance) VALUES (2, 'Aluno Teste', 'aluno.teste@puc.edu', '55566677788', '99887766', 'Rua Estudante, 789', 1, 'Ciência da Computação', '$2a$10$variouscharacters......................', 500.00);

-- Insert sample Company
-- Password 'password' encoded
INSERT INTO company (id, name, email, password) VALUES (3, 'Empresa Parceira', 'contato@empresaparceira.com', '$2a$10$variouscharacters......................');

-- Insert sample Advantages (linked to Company 3)
INSERT INTO advantage (id, name, description, image_url, cost_in_coins, company_id) VALUES (1, 'Desconto em Café', '10% de desconto em qualquer café.', 'placeholder_cafe.png', 50.00, 3);
INSERT INTO advantage (id, name, description, image_url, cost_in_coins, company_id) VALUES (2, 'Ingresso de Cinema', 'Um ingresso grátis para qualquer filme.', 'placeholder_cinema.png', 200.00, 3);
