package com.fst.sir.service.gerant.service.impl;

import com.fst.sir.bean.Formation;
import com.fst.sir.bean.Panier;
import com.fst.sir.bean.ProduitPanierItem;
import com.fst.sir.dao.PanierDao;
import com.fst.sir.enums.EtatCommande;
import com.fst.sir.security.bean.User;
import com.fst.sir.security.common.SecurityUtil;
import com.fst.sir.service.admin.facade.FormationAdminService;
import com.fst.sir.service.client.facade.ProduitPanierItemService;
import com.fst.sir.service.gerant.service.facade.PanierGerantService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanierGerantServiceImpl implements PanierGerantService {

    @Autowired
    private PanierDao panierDao;

    @Autowired
    private FormationAdminService formationAdminService;
    @Autowired
    private ProduitPanierItemService produitPanierItemService;

    @Override
    public List<Panier> findAll() {
        return panierDao.findAll();
    }

    @Override
    public List<Panier> findByEtatCommande(EtatCommande etatCommande) {
        return panierDao.findByEtatCommande(etatCommande);
    }

    @Override
    public List<Panier> findByUserUsername(String username) {
        return panierDao.findByUserUsername(username);
    }

    @Override
    public Panier findByReference(String reference) {
        return panierDao.findByReference(reference);
    }

    @Override
    public int deleteByReference(String reference) {
        return 0;
    }

    @Override
    public Panier save(Panier panier) {
        User user = SecurityUtil.getCurrentUser();
        if (user == null) {
            return null;
        }
        else {
            panier.setUser(user);
            Formation formation = formationAdminService.findByNom(panier.getFormation().getNom());
            if (formation != null) {
                panier.setFormation(formation);
            }
            panier.setDateAjout(new Date());
            panier.setEtatCommande(EtatCommande.EN_TRAITMENT);
            panier.setPrixTotal(formation.getPrix());
            Panier panier1 = panierDao.save(panier);
            if(panier.getProduitPanierItems()!=null){
                List<ProduitPanierItem> produitPanierItemList = new ArrayList<>();
                panier.getProduitPanierItems().forEach(e->  produitPanierItemList.add(produitPanierItemService.save(e)) );
                produitPanierItemList.forEach(e-> panier1.setPrixTotal(e.getPrix() + panier1.getPrixTotal()));
                return panier1;
            }
            return panier;
        }
    }

    @Override
    public Panier update(Panier panier) {
        Panier entity = findByReference(panier.getReference());
        User user = SecurityUtil.getCurrentUser();
        if (user == null || entity == null) {
            return null;
        }
        else {
            panier.setId(entity.getId());
            panier.setUser(user);
            Formation formation = formationAdminService.findByNom(panier.getFormation().getNom());
            if (formation != null) {
                panier.setFormation(formation);
            }
            panier.setDateAjout(new Date());
            panier.setEtatCommande(EtatCommande.EN_TRAITMENT);
            panier.setPrixTotal(formation.getPrix());
            Panier panier1 = panierDao.save(panier);
            if(panier.getProduitPanierItems()!=null){
                List<ProduitPanierItem> produitPanierItemList = new ArrayList<>();
                panier.getProduitPanierItems().forEach(e->  produitPanierItemList.add(produitPanierItemService.save(e)) );
                produitPanierItemList.forEach(e-> panier1.setPrixTotal(e.getPrix() + panier1.getPrixTotal()));
                return panier1;
            }
            return panier;
        }

    }

}
