-- Users definition
CREATE TABLE users (
	id bigserial NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	responsibility varchar(255) NOT NULL,
	email varchar(255) NULL,
	secret varchar(255) NOT NULL,
	is_enabled bool NOT NULL,
	is_account_non_expired bool NOT NULL,
	is_account_non_locked bool NOT NULL,
	is_credentials_non_expired bool NOT NULL,
	CONSTRAINT users_email_unique UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_role_check CHECK (((responsibility)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying])::text[])))
);

-- Word definition
CREATE TABLE word (
	id bigserial NOT NULL,
	user_id int8 NOT NULL,
	word varchar(255) NOT NULL,
	kind varchar(255) NOT NULL,
	english_translations _varchar NOT NULL,
	pronunciations _varchar NOT NULL,
	german_example varchar(255) NOT NULL,
	english_example varchar(255) NOT NULL,
	matches _varchar NOT NULL,
	german_example_recording_url _varchar NULL,
	recording_urls _varchar NULL,
	is_ready bool NOT NULL,
	CONSTRAINT word_word_and_kind_unique UNIQUE (word, kind),
	CONSTRAINT word_pkey PRIMARY KEY (id),
	CONSTRAINT word_type_check CHECK (((kind)::text = ANY ((ARRAY['NOUN'::character varying, 'ADJECTIVE'::character varying, 'VERB'::character varying, 'ADVERB'::character varying, 'COMMON_EXPRESSION'::character varying])::text[]))),
    CONSTRAINT FK_user_word FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Noun definition
CREATE TABLE noun (
	id int8 NOT NULL,
	plural varchar(255) NOT NULL,
	gender varchar(255) NOT NULL,
	CONSTRAINT noun_gender_check CHECK (((gender)::text = ANY ((ARRAY['MASCULINE'::character varying, 'FEMENINE'::character varying, 'NEUTER'::character varying])::text[]))),
	CONSTRAINT noun_pkey PRIMARY KEY (id),
	CONSTRAINT FK_word_noun FOREIGN KEY (id) REFERENCES word(id)
);

-- Adverb definition
CREATE TABLE adverb (
	id int8 NOT NULL,
	CONSTRAINT adverb_pkey PRIMARY KEY (id),
    CONSTRAINT FK_word_adverb FOREIGN KEY (id) REFERENCES word(id)
);

-- Verb definition
CREATE TABLE verb (
	id int8 NOT NULL,
	has_prefix bool NOT NULL,
	is_regular bool NOT NULL,
	is_separable bool NOT NULL,
	CONSTRAINT verb_pkey PRIMARY KEY (id),
    CONSTRAINT FK_word_verb FOREIGN KEY (id) REFERENCES word(id)
);

-- Adjective definition
CREATE TABLE adjective (
	id int8 NOT NULL,
	is_comparable bool NOT NULL,
	comparative varchar(255) NULL,
	superlative varchar(255) NULL,
	CONSTRAINT adjective_pkey PRIMARY KEY (id),
    CONSTRAINT FK_word_adjective FOREIGN KEY (id) REFERENCES word(id)
);

-- Common Expression definition
CREATE TABLE common_expression (
    id int8 NOT NULL,
    CONSTRAINT common_expression_pkey PRIMARY KEY(id),
    CONSTRAINT FK_word__common_expression FOREIGN KEY (id) REFERENCES word(id)
);
-- Dictionary word definition
CREATE TABLE dictionary_word (
	id bigserial NOT NULL,
	word varchar(128) NOT NULL,
	kind varchar(255) NOT NULL,
	english_translations _varchar NOT NULL,
	pronunciations _varchar NOT NULL,
	noun_gender varchar(255) NULL,
	noun_plural varchar(255) NULL,
	adjective_comparable bool NULL,
	adjective_comparative varchar(255) NULL,
	adjective_superlative varchar(255) NULL,
	verb_regular bool NULL,
	verb_separable bool NULL,
	verb_with_prefix bool NULL,
	recordingurls _varchar NULL,
	scrap_result int2 NULL,
	CONSTRAINT dictionary_word_noun_gender_check CHECK (((noun_gender)::text = ANY ((ARRAY['MASCULINE'::character varying, 'FEMENINE'::character varying, 'NEUTER'::character varying])::text[]))),
	CONSTRAINT dictionary_word_pkey PRIMARY KEY (id),
	CONSTRAINT dictionary_word_scrap_result_check CHECK (((scrap_result >= 0) AND (scrap_result <= 2))),
	CONSTRAINT dictionary_word_type_check CHECK (((kind)::text = ANY ((ARRAY['NOUN'::character varying, 'ADJECTIVE'::character varying, 'VERB'::character varying, 'ADVERB'::character varying, 'COMMON_EXPRESSION'::character varying])::text[])))
);

-- Practice session definition
CREATE TABLE practice_session (
	id bigserial NOT NULL,
	CONSTRAINT practice_session_pkey PRIMARY KEY(id)
);

-- Practice session detail definition
CREATE TABLE practice_session_detail (
	id bigserial NOT NULL,
	practice_session_id int8 NOT NULL,
	word_id int8 NOT NULL,
	CONSTRAINT practice_session_detail_pkey PRIMARY KEY (id),
	CONSTRAINT practice_session_detail__word_id_and_practice_session_id UNIQUE (word_id, practice_session_id),
	CONSTRAINT FK_practice_session__practice_session_detail FOREIGN KEY (practice_session_id) REFERENCES practice_session(id),
	CONSTRAINT FK_word__practice_session_detail FOREIGN KEY (word_id) REFERENCES word(id)
);

-- Practice session result definition
CREATE TABLE practice_session_result (
	id bigserial NOT NULL,
	user_id int8 NOT NULL,
	score decimal(3, 2) NOT NULL,
	left_swipes_count int8 NOT NULL,
	right_swipes_count int8 NOT NULL,
	words_tested_count int8 NOT NULL,
	duration_in_seconds int8 NOT NULL,
	created_at timestamptz(6) NOT NULL,
	CONSTRAINT practice_session_result_pkey PRIMARY KEY (id),
	CONSTRAINT practice_session_result_score_check CHECK (((score <= (5)::decimal) AND (score >= (0)::decimal))),
	CONSTRAINT FK_user__practice_session_result  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Practice session result detail definition
CREATE TABLE practice_session_result_detail (
	id bigserial NOT NULL,
	left_swipe bool NOT NULL,
	right_swipe bool NOT NULL,
	word_id int8 NOT NULL,
	practice_session_result_id int8 NOT NULL,
	CONSTRAINT ps_result_detail__id_and_word_id__unique  UNIQUE (practice_session_result_id, word_id),
	CONSTRAINT practice_session_result_detail_pkey PRIMARY KEY (id),
	CONSTRAINT FK_ps_result__ps_result_detail  FOREIGN KEY (practice_session_result_id) REFERENCES practice_session_result(id),
	CONSTRAINT FK_word_ps__ps_result_detail FOREIGN KEY (word_id) REFERENCES word(id)
);
