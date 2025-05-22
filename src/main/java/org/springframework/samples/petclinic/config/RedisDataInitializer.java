package org.springframework.samples.petclinic.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.RedisOwnerRepository;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.vet.RedisVetRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Initialize Redis with sample data.
 */
@Configuration
public class RedisDataInitializer {

    @Bean
    public CommandLineRunner initData(RedisOwnerRepository ownerRepository, RedisVetRepository vetRepository,
            RedisTemplate<String, Object> redisTemplate) {
        return args -> {
            // Clear existing data
            redisTemplate.delete("owners");
            redisTemplate.delete("vets");
            redisTemplate.delete("pet_types");
            redisTemplate.delete("owner_id_sequence");
            redisTemplate.delete("vet_id_sequence");

            // Create pet types
            PetType dog = new PetType();
            dog.setName("dog");

            PetType cat = new PetType();
            cat.setName("cat");

            PetType bird = new PetType();
            bird.setName("bird");

            PetType lizard = new PetType();
            lizard.setName("lizard");

            PetType snake = new PetType();
            snake.setName("snake");

            PetType hamster = new PetType();
            hamster.setName("hamster");

            // Save pet types to Redis
            redisTemplate.opsForSet().add("pet_types", dog, cat, bird, lizard, snake, hamster);

            // Create specialties
            Specialty radiology = new Specialty();
            radiology.setName("radiology");

            Specialty surgery = new Specialty();
            surgery.setName("surgery");

            Specialty dentistry = new Specialty();
            dentistry.setName("dentistry");

            // Create vets
            Vet vet1 = new Vet();
            vet1.setFirstName("James");
            vet1.setLastName("Carter");
            vetRepository.save(vet1);

            Vet vet2 = new Vet();
            vet2.setFirstName("Helen");
            vet2.setLastName("Leary");
            vet2.addSpecialty(radiology);
            vetRepository.save(vet2);

            Vet vet3 = new Vet();
            vet3.setFirstName("Linda");
            vet3.setLastName("Douglas");
            vet3.addSpecialty(surgery);
            vet3.addSpecialty(dentistry);
            vetRepository.save(vet3);

            Vet vet4 = new Vet();
            vet4.setFirstName("Rafael");
            vet4.setLastName("Ortega");
            vet4.addSpecialty(surgery);
            vetRepository.save(vet4);

            Vet vet5 = new Vet();
            vet5.setFirstName("Henry");
            vet5.setLastName("Stevens");
            vet5.addSpecialty(radiology);
            vetRepository.save(vet5);

            Vet vet6 = new Vet();
            vet6.setFirstName("Sharon");
            vet6.setLastName("Jenkins");
            vetRepository.save(vet6);

            // Create owners, pets, and visits
            Owner owner1 = new Owner();
            owner1.setFirstName("George");
            owner1.setLastName("Franklin");
            owner1.setAddress("110 W. Liberty St.");
            owner1.setCity("Madison");
            owner1.setTelephone("6085551023");

            Pet pet1 = new Pet();
            pet1.setName("Leo");
            pet1.setBirthDate(LocalDate.now().minusYears(2));
            pet1.setType(cat);
            owner1.addPet(pet1);

            ownerRepository.save(owner1);

            Owner owner2 = new Owner();
            owner2.setFirstName("Betty");
            owner2.setLastName("Davis");
            owner2.setAddress("638 Cardinal Ave.");
            owner2.setCity("Sun Prairie");
            owner2.setTelephone("6085551749");

            Pet pet2 = new Pet();
            pet2.setName("Basil");
            pet2.setBirthDate(LocalDate.now().minusYears(1));
            pet2.setType(hamster);
            owner2.addPet(pet2);

            ownerRepository.save(owner2);

            Owner owner3 = new Owner();
            owner3.setFirstName("Eduardo");
            owner3.setLastName("Rodriquez");
            owner3.setAddress("2693 Commerce St.");
            owner3.setCity("McFarland");
            owner3.setTelephone("6085558763");

            Pet pet3 = new Pet();
            pet3.setName("Rosy");
            pet3.setBirthDate(LocalDate.now().minusYears(3));
            pet3.setType(dog);
            owner3.addPet(pet3);

            ownerRepository.save(owner3);

            Owner owner4 = new Owner();
            owner4.setFirstName("Harold");
            owner4.setLastName("Davis");
            owner4.setAddress("563 Friendly St.");
            owner4.setCity("Windsor");
            owner4.setTelephone("6085553198");

            Pet pet4 = new Pet();
            pet4.setName("Jewel");
            pet4.setBirthDate(LocalDate.now().minusYears(1));
            pet4.setType(dog);
            owner4.addPet(pet4);

            ownerRepository.save(owner4);

            Owner owner5 = new Owner();
            owner5.setFirstName("Peter");
            owner5.setLastName("McTavish");
            owner5.setAddress("2387 S. Fair Way");
            owner5.setCity("Madison");
            owner5.setTelephone("6085552765");

            Pet pet5 = new Pet();
            pet5.setName("George");
            pet5.setBirthDate(LocalDate.now().minusYears(4));
            pet5.setType(snake);
            owner5.addPet(pet5);

            ownerRepository.save(owner5);

            Owner owner6 = new Owner();
            owner6.setFirstName("Jean");
            owner6.setLastName("Coleman");
            owner6.setAddress("105 N. Lake St.");
            owner6.setCity("Monona");
            owner6.setTelephone("6085552654");

            Pet pet6 = new Pet();
            pet6.setName("Max");
            pet6.setBirthDate(LocalDate.now().minusYears(2));
            pet6.setType(cat);
            owner6.addPet(pet6);

            Pet pet7 = new Pet();
            pet7.setName("Samantha");
            pet7.setBirthDate(LocalDate.now().minusYears(1));
            pet7.setType(cat);
            owner6.addPet(pet7);

            ownerRepository.save(owner6);

            // Add visits
            Visit visit1 = new Visit();
            visit1.setDate(LocalDate.now().minusDays(5));
            visit1.setDescription("Sneezing");
            pet7.addVisit(visit1);

            ownerRepository.save(owner6);
        };
    }
}
