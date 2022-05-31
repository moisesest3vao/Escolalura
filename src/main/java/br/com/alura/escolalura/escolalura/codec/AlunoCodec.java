package br.com.alura.escolalura.escolalura.codec;

import br.com.alura.escolalura.escolalura.model.Aluno;
import br.com.alura.escolalura.escolalura.model.Curso;
import br.com.alura.escolalura.escolalura.model.Habilidade;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlunoCodec implements CollectibleCodec<Aluno> {

    private Codec<Document> codec;

    public AlunoCodec(Codec<Document> codec){
        this.codec = codec;
    }

    @Override
    public Aluno generateIdIfAbsentFromDocument(Aluno aluno) {
        return documentHasId(aluno) ? aluno.criarId() : aluno;
    }

    @Override
    public boolean documentHasId(Aluno aluno) {
        return aluno.getId()==null;
    }

    @Override
    public BsonValue getDocumentId(Aluno aluno) {
        if(!documentHasId(aluno)){
            throw new IllegalStateException("This document has no id");
        }
        return new BsonString(aluno.getId().toHexString());
    }

    @Override
    public Aluno decode(BsonReader reader, DecoderContext decoder) {
        Document document = codec.decode(reader, decoder);
        Aluno aluno = new Aluno();

        aluno.setId(document.getObjectId("_id"));
        aluno.setNome(document.getString("nome"));
        aluno.setDataNascimento(document.getDate("data_nascimento"));
        Document curso = (Document) document.get("curso");

        if(curso != null){
            String nomeCurso = curso.getString("nome");
            aluno.setCurso(new Curso(nomeCurso));
        }

        return aluno;
    }

    @Override
    public void encode(BsonWriter writer, Aluno aluno, EncoderContext encoder) {
        ObjectId id = aluno.getId();
        String nome = aluno.getNome();
        Date dataNascimento = aluno.getDataNascimento();
        Curso curso = aluno.getCurso();
        List<Habilidade> habilidades = aluno.getHabilidades();

        Document document = new Document();
        document.put("_id", id);
        document.put("nome", nome);
        document.put("data_nascimento", dataNascimento);
        document.put("curso", new Document("nome", curso.getNome()));
        if(habilidades != null){
            List<Document> habilidadesDocument = new ArrayList<>();
            for(Habilidade hab : habilidades){
                habilidadesDocument.add(new Document("nome", hab.getNome())
                        .append("nivel", hab.getNivel()));
            }

            document.put("habilidades", habilidadesDocument);
        }
        this.codec.encode(writer,document,encoder);
    }

    @Override
    public Class<Aluno> getEncoderClass() {
        return Aluno.class;
    }
}
