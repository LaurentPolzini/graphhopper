# Tâche 3 - Rapport - MOUSSA DIABATE & POLZIN

Cette tâche portait sur les Github Actions et sur l'utilisation de mocks, grâce à la librairie Mockito.

## Modification du workflow - ajout de Github Actions

Une action permettant de ne pas accepter un "git push" dont le score de mutation est inférieur au score précédent a été ajoutée.
Pour trouver les scores précédents, nous utilisons les balises : "workflow_search: true" qui va chercher dans les résultats précédents et "workflow_conclusion: success" qui permet de filtrer uniquement les workflow qui ont fonctionnés. Le premier run n'a par définition pas de workflow qui le précède, on utilise alors "continue-on-error: true" pour ce premier tour.

On aimerai désormais générer le score PIT et en extraire les données pertinentes, ici le nombre de mutations tuées, cela grâce à un script shell : 
        
        mvn clean test-compile -pl core org.pitest:pitest-maven:mutationCoverage | tee core/core-score

        Core_Score=$(grep -A15 Statistics core/core-score | grep Killed | grep -oP '\(\K[0-9]+')
        echo "le score de mutation pour le module core est $Core_Score%" 

        if [ -f core-artefact/Prec_Score ]; then
        Prev_Score=$(cat core-artefact/Prec_Score)
        else
        Prev_Score=0
        fi

        if [ "$Core_Score" -lt "$Prev_Score" ]; then
        echo "Le score de mutation dans le module core a baisse $Prev_Score% ---> $Core_Score%"
        exit 1
        fi
        echo "ancien score: $Prev_Score%, nouveau_score: $Core_Score%"
        echo "$Core_Score" > ./Prec_Score

Ce script permet dans un premier temps d'extraire le score de mutation, puis de le comparer au score juste précédent et de prendre une décision en fonction du résultat. Si le score est inférieur, on sort avec "exit 1". Sinon, l'ancien score est mit à jour et prend la valeur du score de mutation calculé.

Ce script ne fonctionne que sur le module "core". Un second script, similaire à celui-ci, permettra de faire la même chose pour le module "reader-gtfs".

Il ne faut pas non plus oublier les artefacts et les mettre à jour : exemple pour le module "core" : 

        - name: 'Upload precedent core-score'
            uses: actions/upload-artifact@v4
            with:
            name: core-artefact-${{ matrix.java-version }}
            path: ./Prec_Score


## Mockito




