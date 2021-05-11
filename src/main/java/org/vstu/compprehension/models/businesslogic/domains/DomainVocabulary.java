package org.vstu.compprehension.models.businesslogic.domains;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFSyntax;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.jetbrains.annotations.NotNull;
import org.vstu.compprehension.models.businesslogic.Concept;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DomainVocabulary {
    String vocabularyPath;
    Model model;

    public DomainVocabulary(String vocabularyPath) {
        this.vocabularyPath = vocabularyPath;
        model = ModelFactory.createDefaultModel();

        // read an RDF file
        model.read(vocabularyPath);

        ////    If the syntax is not as the file extension, a language can be declared:
        //    model.read("data.foo", "TURTLE") ;
    }

    public List<Concept> readConcepts() {

        HashMap<String, HashSet<String>> conceptName2bases = new HashMap<>();

        //Resource Concept = model.getResource(SKOS.Concept);
        Resource ConceptClass = SKOS.Concept;

        // find all [top] concepts and recurse from them
//        ResIterator iter = model.listSubjectsWithProperty(RDFS.subClassOf, ConceptClass);
        ResIterator iter = model.listSubjectsWithProperty(RDF.type);
        while (iter.hasNext()) {
            Resource conceptNode = iter.nextResource();
            readConceptFromResource(conceptNode, conceptName2bases, null);
        }


        // make concepts
        HashMap<String, Concept> concepts = new HashMap<>();

        // make concepts in order to create independent earlier
        while (! conceptName2bases.isEmpty()) {
            boolean nothingFound = true;  // check for circular dependencies
            for (String name : new HashSet<String>(conceptName2bases.keySet())) {
                HashSet<String> bases = conceptName2bases.get(name);
                if (bases.isEmpty() || concepts.keySet().containsAll(bases)) {
                    /// System.out.println("Create new: " + name + " - " +  bases);

                    // copy base concepts to new list ...
                    ArrayList<Concept> baseConcepts = new ArrayList<>();
                    for (String base : bases) {
                        baseConcepts.add(concepts.get(base));
                    }
                    // create concept with base concepts that already exist
                    concepts.put(name, new Concept(name, baseConcepts));

                    conceptName2bases.remove(name);
                    nothingFound = false;
                }
            }
            if (nothingFound) {
                throw new RuntimeException("Error reading concepts from file: " + this.vocabularyPath + "\n\tThe following concepts are interdependent and cannot be created:\n\t" + (conceptName2bases.keySet().toString()));
            }
        }

        return new ArrayList<>(concepts.values());
    }

    /**
     * @param conceptNode RDFNode of concept
     * @param conceptName2bases [in-out]
     * @param baseConceptName [optional]
     */
    protected void readConceptFromResource(@NotNull Resource conceptNode, @NotNull HashMap<String, HashSet<String>> conceptName2bases, String baseConceptName) {
        String name = conceptNode.getLocalName();

        /// System.out.println("adding: " + name + " - " +  baseConceptName);

        // other features like prefLabel are ignored so far

        boolean shouldNotRecurse = conceptName2bases.containsKey(name);
        conceptName2bases.putIfAbsent(name, new HashSet<>());
        if (baseConceptName != null) {
            conceptName2bases.get(name).add(baseConceptName);
        }

        if (shouldNotRecurse)
            return;

        // find all child concepts
        ResIterator iter = model.listSubjectsWithProperty(RDFS.subClassOf, conceptNode);
        while (iter.hasNext()) {
            Resource childConceptNode = iter.nextResource();
            readConceptFromResource(childConceptNode, conceptName2bases, name);
        }
    }

    /** Find all direct & indirect subclasses of given class
     *  */
    public List<String> classDescendants(String className) {
        ArrayList<String> result = new ArrayList<>();
        addClassDescendants(className, result, -1);
        return result;
    }

    /** Extends given list with local names of subclasses till given depth limit.
     * Intended for internal use but can be utilized as-is.
     * maxDepth unlimited search if < 0; get only direct children if == 1.
     *  */
    public void addClassDescendants(String className, List<String> classes, int maxDepth) {
        if (maxDepth == 0)
            return;
        // find all child classes
        String ns = model.getNsPrefixURI("");
        Resource classNode = model.getResource(ns + className);
        ResIterator iter = model.listSubjectsWithProperty(RDFS.subClassOf, classNode);
        while (iter.hasNext()) {
            Resource childClassNode = iter.nextResource();
            String childClassName = childClassNode.getLocalName();
            if (!classes.contains(childClassName)) {
                classes.add(childClassName);
            }
            addClassDescendants(childClassName, classes, maxDepth - 1);
        }
    }


    /// debug
    public static void main(String[] args) {
        DomainVocabulary voc = new DomainVocabulary("c:\\D\\Work\\YDev\\CompPr\\world_onto\\domain_schema.ttl");
        voc.readConcepts();
    }
}
