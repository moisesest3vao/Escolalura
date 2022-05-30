package br.com.alura.escolalura.escolalura.repository;

import br.com.alura.escolalura.escolalura.codec.AlunoCodec;
import br.com.alura.escolalura.escolalura.model.Aluno;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AlunoRepository {
    private MongoClient client;
    private MongoDatabase db;

    private void createConnection() {
        Codec<Document> documentCodec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        AlunoCodec alunoCodec = new AlunoCodec(documentCodec);

        CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(alunoCodec));

        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(registro).build();

        this.client = new MongoClient("localhost:27017", options);
        this.db = client.getDatabase("test");
    }

    public void save(Aluno aluno){
        createConnection();
        MongoCollection<Aluno> alunos = this.db.getCollection("alunos", Aluno.class);
        alunos.insertOne(aluno);
        client.close();
    }

    public List<Aluno> findAll() {
        createConnection();
        MongoCollection<Aluno> alunos = this.db.getCollection("alunos", Aluno.class);
        MongoCursor<Aluno> iterator = alunos.find().iterator();
        List<Aluno> alunosList = new ArrayList<>();

        while(iterator.hasNext()){
            System.out.println(iterator.next());
            Aluno atual = iterator.next();
            alunosList.add(atual);
        }
        client.close();
        return alunosList;
    }
}
