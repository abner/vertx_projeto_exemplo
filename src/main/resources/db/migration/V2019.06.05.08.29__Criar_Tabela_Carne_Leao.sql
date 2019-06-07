CREATE TABLE carne_leao (
    cpf VARCHAR(11) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    ano_calendario int NOT NULL,
    conteudo jsonb,
    CONSTRAINT carne_leao_pk PRIMARY KEY(cpf, ano_calendario)
);

