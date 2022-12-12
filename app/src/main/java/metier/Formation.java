package metier;
public class Formation
{
    String libelle, niveau, type, description;
    int coutRevient, diplomante, duree;

    public Formation()
    {
    }
    public Formation(String libelle, String niveau, String type, String description, int coutRevient, int diplomante, int duree)
    {
        this.libelle = libelle;
        this.niveau = niveau;
        this.type = type;
        this.description = description;
        this.coutRevient = coutRevient;
        this.diplomante = diplomante;
        this.duree = duree;
    }

    public String getLibelle()
    {
        return libelle;
    }
    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    public String getNiveau()
    {
        return niveau;
    }
    public void setNiveau(String niveau)
    {
        this.niveau = niveau;
    }

    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getCoutRevient()
    {
        return coutRevient;
    }
    public void setCoutRevient(int coutRevient)
    {
        this.coutRevient = coutRevient;
    }

    public int getDiplomante()
    {
        return diplomante;
    }
    public void setDiplomante(int diplomante)
    {
        this.diplomante = diplomante;
    }

    public int getDuree()
    {
        return duree;
    }
    public void setDuree(int duree)
    {
        this.duree = duree;
    }
}
